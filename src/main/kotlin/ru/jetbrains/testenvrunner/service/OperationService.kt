package ru.jetbrains.testenvrunner.service

import org.springframework.stereotype.Service
import ru.jetbrains.testenvrunner.exception.NotFoundOperationException
import ru.jetbrains.testenvrunner.model.ExecuteOperation
import ru.jetbrains.testenvrunner.model.ExecuteResult
import ru.jetbrains.testenvrunner.model.OperationStatus
import ru.jetbrains.testenvrunner.repository.OperationRepository
import ru.jetbrains.testenvrunner.utils.generateRandomWord

@Service
final class OperationService(val operationRepository: OperationRepository) {
    companion object {
        lateinit var operationService: OperationService
    }

    init {
        operationService = this
    }

    private val operations: MutableMap<String, ExecuteOperation> = mutableMapOf()

    fun create(command: String, directory: String = "", keepInSystem: Boolean = true): ExecuteOperation {
        val id = generateRandomWord()
        val executeOperation = ExecuteOperation(command, directory, ExecuteResult(), OperationStatus.CREATED, id,
                keepInSystem)
        operations[id] = executeOperation
        return executeOperation
    }

    fun removeAll(removedIds: List<String>) {
        removedIds.forEach { remove(it) }
    }

    private fun remove(operationId: String) {
        removeFromMemory(operationId)
        operationRepository.delete(operationId)
    }

    fun get(operationId: String): ExecuteOperation {
        var operation = operations[operationId]
        if (operation == null)
            operation = operationRepository.findById(operationId)
        return operation ?: throw NotFoundOperationException(operationId)
    }

    fun getList(operationIds: List<String>): List<ExecuteOperation> {
        return operationIds.map { get(it) }
    }

    fun fail(operation: ExecuteOperation) {
        operation.status = OperationStatus.FAILED
        complete(operation)
        println("Operation fails during performing $operation")
    }

    fun success(operation: ExecuteOperation) {
        operation.status = OperationStatus.SUCCESS
        complete(operation)
        println("Operation successfully performed $operation")
    }

    fun complete(operation: ExecuteOperation) {
        if (operation.keepInSystem)
            operationRepository.save(operation)
        removeFromMemory(operation.id)
    }

    private fun removeFromMemory(operationId: String) {
        operations.remove(operationId)
    }

    fun isCompleted(operationId: String): Boolean {
        return !operations.containsKey(operationId)
    }

    fun isFailed(operationId: String): Boolean {
        val operation = get(operationId)
        return operation.status == OperationStatus.FAILED
    }
}


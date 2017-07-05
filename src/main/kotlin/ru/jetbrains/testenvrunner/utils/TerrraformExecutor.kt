package ru.jetbrains.testenvrunner.utils

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import ru.jetbrains.testenvrunner.model.ExecutionCommand
import ru.jetbrains.testenvrunner.model.ExecutionResult

@Component
class TerrraformExecutor {
    @Value("\${script.terraform}")
    private val terraformRunScript: String = "echo script is not available"

    fun executeTerraformScript(): ExecutionResult {
        return BashExecutor.executeCommand(ExecutionCommand(terraformRunScript))
    }
}
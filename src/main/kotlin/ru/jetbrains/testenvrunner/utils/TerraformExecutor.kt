package ru.jetbrains.testenvrunner.utils

import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import org.springframework.stereotype.Service
import ru.jetbrains.testenvrunner.model.ExecutionCommand
import ru.jetbrains.testenvrunner.model.ExecutionResult
import ru.jetbrains.testenvrunner.model.TerraformScript

@Service
class TerraformExecutor constructor(val bashExecutor: BashExecutor) {
    /**
     * Plan and apply terraform script
     * @param script scrpit that will be run
     */
    fun executeTerraformScript(script: TerraformScript): ExecutionResult {
        return bashExecutor.executeCommand(ExecutionCommand("terraform apply -no-color"), directory = script.absolutePath)
    }

    /**
     * Destroy the run infrastructure
     * @param script the script that will be stopped
     */
    fun destroyTerraformScript(script: TerraformScript): ExecutionResult {
        return bashExecutor.executeCommand(ExecutionCommand("terraform destroy -no-color -force ${script.absolutePath}"), directory = script.absolutePath)
    }

    /**
     * Check the script is run
     * @param script checked script
     * @return is run or no
     */
    fun isScriptRun(script: TerraformScript): Boolean {
        val result = bashExecutor.executeCommand(ExecutionCommand("terraform state list"), directory = script.absolutePath)
        val msg: String = "No state file was found"
        if (result.exitValue != 0 && !result.output.contains(msg)) throw Exception("error during check of script state\n ${result.output}")
        return !result.output.isEmpty() && !result.output.contains(msg)
    }

    fun getLink(script: TerraformScript): String {
        val result = bashExecutor.executeCommand(ExecutionCommand("terraform output -no-color -json"), directory = script.absolutePath)
        if (result.exitValue != 0) return ""
        val parser: Parser = Parser()
        val json: JsonObject = parser.parse(StringBuilder(result.output)) as JsonObject
        return ((json["link"] as JsonObject)["value"] as String?) ?: ""
    }
}
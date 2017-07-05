package ru.jetbrains.testenvrunner.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import ru.jetbrains.testenvrunner.model.ExecutionCommand
import ru.jetbrains.testenvrunner.utils.BashExecutor

@Controller
@RequestMapping("/")
class IndexController {
    @RequestMapping(method = arrayOf(RequestMethod.GET))
    fun doIndexGet(model: Model): String {
        model.addAttribute("command", ExecutionCommand())
        return "index"
    }

    @RequestMapping("/result", method = arrayOf(RequestMethod.POST))
    fun doExecResultPost(command: ExecutionCommand, model: Model): String {
        val result = BashExecutor.executeCommand(command)
        model.addAttribute("result", result)
        return "result"
    }

    @RequestMapping("/result_terraform", method = arrayOf(RequestMethod.POST))
    fun doExecResultTerraformPost(model: Model): String {
        val runTerraformCommand = ExecutionCommand("./src/main/resources/terraform/terraform_run.sh")
        val result = BashExecutor.executeCommand(runTerraformCommand)
        model.addAttribute("result", result)
        return "result"
    }
}
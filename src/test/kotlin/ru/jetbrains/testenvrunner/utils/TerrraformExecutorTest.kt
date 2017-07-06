package ru.jetbrains.testenvrunner.utils

import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import java.io.File
import javax.inject.Inject

@RunWith(SpringRunner::class)
@SpringBootTest
class TerrraformExecutorTest : Assert() {
    @Inject
    lateinit var terraformExecurtor: TerrraformExecutor

    @Test
    fun executeTerraformScriptSuccess() {
        val result = terraformExecurtor.executeTerraformScript(File("hello-world-config.tfplan"))
        assertTrue("The terraform script run fail. Exit code: ${result.exitValue}", result.exitValue == 0)
    }
}
provider "docker" {
}

resource "docker_image" "mysql" {
  name = "mysql/mysql-server:${var.version}"
  keep_locally = true
}

resource "docker_container" "mysql" {
  image = "${docker_image.mysql.latest}"
  name = "${var.name}"
  hostname = "mysql"
  must_run = true
  ports {
    internal = 3306
    external = "${var.external_port}"
  }
  env = [
    "MYSQL_ROOT_PASSWORD=sa",
    "MYSQL_USER=mysql",
    "MYSQL_PASSWORD=mysql",
    "MYSQL_DATABASE=sushe"]
}

output "link" {
  value = "testenv.labs.intellij.net:${var.external_port}"
}
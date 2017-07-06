provider "docker" {
}
resource "docker_image" "ubuntu" {
  name = "ubuntu:precise"
}
resource "docker_container" "ubuntu" {
  name = "hello-server"
  image = "${docker_image.ubuntu.latest}"
  ports {
    internal = 4000
    external = 4001
  }
}
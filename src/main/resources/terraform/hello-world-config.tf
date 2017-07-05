provider "docker" {
}
resource "docker_image" "hello" {
  name = "nashikhmin/node-hello"
}
resource "docker_container" "hello" {
  name = "hello-server"
  image = "${docker_image.hello.latest}"
  ports {
    internal = 4000
    external = 4001
  }
}
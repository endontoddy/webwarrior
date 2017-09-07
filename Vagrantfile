Vagrant.configure("2") do |config|
  config.vm.provider "docker" do |d|
    d.build_dir = "."
    d.remains_running = true
    d.has_ssh = true
    d.ports = ["8080:80"]
  end
  config.ssh.insert_key = false

  config.vm.synced_folder ".", "/app"
end
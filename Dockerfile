FROM linkyard/docker-sbt

RUN apt-get update && apt-get install apt-utils openssh-server lighttpd nodejs nodejs-legacy sudo -y

RUN service ssh start
EXPOSE 22
EXPOSE 80

COPY ./lighttpd.conf /etc/lighttpd/lighttpd.conf
RUN service lighttpd start

# Create Vagrant user
RUN useradd -m vagrant -p k0eaf6alWzgNU
RUN usermod -aG sudo vagrant
RUN chsh -s /bin/bash vagrant
RUN chsh -s /bin/bash root
RUN mkdir /home/vagrant/.ssh
RUN chmod 700 /home/vagrant/.ssh
COPY ./insecure_key /home/vagrant/.ssh/id_rsa
COPY ./authorized_keys /home/vagrant/.ssh/authorized_keys
RUN chmod 600 /home/vagrant/.ssh/id_rsa
RUN chmod 600 /home/vagrant/.ssh/authorized_keys
RUN chown -R vagrant:vagrant /home/vagrant/.ssh
RUN echo "vagrant ALL=(ALL) NOPASSWD: ALL" >> /etc/sudoers
RUN echo 'PATH=/usr/local/sbt/bin:$PATH' >> /home/vagrant/.bashrc
RUN echo 'cd /app' >> /home/vagrant/.bashrc

COPY ./docker_start.sh /usr/sbin/startup.sh
RUN chmod +x /usr/sbin/startup.sh
#RUN sudo -u vagrant /usr/local/sbt/bin/sbt

CMD "/usr/sbin/startup.sh"
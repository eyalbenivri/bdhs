#!/bin/bash

function prep {
    echo "export HADOOP_USER_NAME=hdfs" >> ~/.bash_profile
    echo "export SPARK_MAJOR_VERSION=2" >> ~/.bash_profile
    yum update -y
    rm -Rf /boot/*.img
}

function install_intellij {
    echo "Installing IntelliJ"
    filename="https://download.jetbrains.com/idea/ideaIC-$1.tar.gz"
    echo "Downloading ${filename}"
    wget -O /tmp/idea.tar.gz $filename
    mkdir /opt/idea
    tar xzvf /tmp/idea.tar.gz --strip 1 -C /opt/idea
}

function install_pycharm {
    echo "Installing PyCharm"
    filename="https://download.jetbrains.com/python/pycharm-community-$1.tar.gz"
    echo "Downloading ${filename}"
    wget -O /tmp/pycharm.tar.gz $filename
    mkdir /opt/pycharm
    tar xzvf /tmp/pycharm.tar.gz --strip 1 -C /opt/pycharm
}

function install_mvn {
    echo "Installing Maven"
    wget -O /tmp/maven.tar.gz http://apache.spd.co.il/maven/maven-3/3.3.9/binaries/apache-maven-3.3.9-bin.tar.gz
    mkdir /opt/maven
    tar -xzvf /tmp/maven.tar.gz --strip 1 -C /opt/maven
    echo "export PATH=$PATH:/opt/maven/bin" >> ~/.bash_profile
    source ~/.bash_profile
}

function install_scala {
    echo "Installing Scala"
    filename="http://downloads.lightbend.com/scala/$1/scala-$1.tgz"
    echo "Downloading ${filename}"
    wget -O /tmp/scala.tar.gz $filename
    mkdir /opt/scala
    tar -xvf /tmp/scala.tar.gz -C /opt/scala
    echo "export SCALA_HOME=/opt/scala" >> ~/.bash_profile
    echo "export PATH=$PATH:$SCALA_HOME/bin" >> ~/.bash_profile
    source ~/.bash_profile
}

function cache_mvn_packages {
    echo "Caching MVN Packages"
    cd ./code/BDHScode
    mvn clean install
    cd ../../
}

function install_gui {
    yum groupinstall -y "GNOME Desktop"
    sed -i 's/id:3:initdefault/id:5:initdefault/g' /etc/inittab
    yum update -y kernel*
    systemctl set-default graphical.target
}

prep
install_mvn
install_scala 2.11.8
install_intellij 2016.3.4
install_pycharm 2016.3.2
cache_mvn_packages
install_gui
reboot

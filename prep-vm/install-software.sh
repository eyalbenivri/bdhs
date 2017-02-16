#!/bin/bash

function install_intellij {
    echo "Installing IntelliJ"
    filename = "https://download.jetbrains.com/idea/ideaIC-$1.tar.gz"
    echo "Downloading ${filename}"
    wget $filename
}

function install_pycharm {
    echo "Installing PyCharm"
    filename = "https://download.jetbrains.com/python/pycharm-community-$1.tar.gz"
    wget "${filename}"
}

function install_mvn {
    echo "Installing Maven"
}

function install_scala {
    echo "Installing Scala"
}

function install_gui {
    yum -y groups install "GNOME Desktop" 
    startx
}

install_mvn
install_scala
install_intellij 2016.3.4
# install_pycharm 2016.3.2
install_gui
restart

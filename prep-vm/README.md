# Prepering the VM
In order to use the VM for BDHS labs, the instructor needs to prepare the VM before packging it.
The steps here start from a base Hortonworks sandbox, and explain how to get the basic VM, to a full functional lab VM.
Since the base VM of Hortonworks, is a server only machine (no GUI) and we want to give all students a unified enviorment (without telling everyone to install JAVA, IDEA, Scala etc.)
We will install all those things on the VM, including GUI using the steps below.
After completing the steps below, you will have a VirtualBox VM ready for distribution.

_Note:_ Be aware that this will be rather large VM (around 12GB) so be ready with external HD or other large files transfer solution.

1. Download and install VirtualBox on your machine. [VirtaulBox website](https://www.virtualbox.org)
1. Download the Hortonworks sandbox image for VirtaulBox. [Hortonworks Website](http://hortonworks.com)
1. Load the Hortonworks VM image into VirtualBox.
    - In VirtualBox, go to 'File', and then 'Import Appliance'
    - Point the file explorer to the downloaded VM from hortonworks
    - Configure the new machine, and complete the import process
1. Boot up the VM and wait for startup to complete
1. SSH into the machine as root, change the password to something memrable (remember, your students will use these credentials during the course, so make it easy enough)
1. execute the following command

    ```bash
    cd /root && git clone https://github.com/eyalbenivri/bdhs.git && cd bdhs && ./prep-vm/install-software.sh
    ```

    this command will run for a few (good) minutes. It will install everything needed for the class, including:
    
    - GUI (GNOME)
    - Maven
    - IntelliJ
    - Scala
    
1. Together with all the other assets in the repository (labs, solutions and data), you should have a ready enviorment.
1. To re-package the VM for distribution turn off the machine, go to the VirtaulBox, go to 'File' and 'Export Appliance' and follow the on-screen instructions
    

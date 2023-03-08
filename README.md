# Config Manager

## How to use

### Register your application to manage
Register your application in the config.properties file. You must provide application's name and path. It's important to keep only one config per line.

#### Example

> kubernetes=kubernetes.properties <br>
> git=git.properties

### Command

#### Structure

> cmanager application_name chosen_configuration 

#### Examples

> cmanager use kubernetes hml <br>
> cmanager use git personal
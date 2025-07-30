# fiserv

Simple to use file-server offering fuzzy-search, tags, accounts and more

# Architecture

Backend:
Spring Boot, Java, REST API

Frontend:
Electron, ReactJS

DB:
MariaDb

# Building and Running

## Docker build

```bash
sudo docker build -t fiserv .
# Run with
# Replace the left portion of the -p arg with the port you want it to be accessible on
sudo docker run -p 8080:8080 fiserv
# Obviously you can also include this in a docker-compose
```

## Linux/Unix

```bash
# Have not tested on systems like OpenBsd etc, but probably works
cd server
./gradlew bootRun
```

## Windows

```powershell
cd server
# Execute the batc script
./gradlew.bat bootRun
```

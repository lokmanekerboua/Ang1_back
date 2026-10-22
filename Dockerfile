FROM ubuntu:latest
LABEL authors="lokma"

ENTRYPOINT ["top", "-b"]
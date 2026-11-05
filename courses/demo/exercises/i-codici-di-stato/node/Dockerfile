# syntax=docker/dockerfile:1
# Builds ./${SRC} (starter or solution) with the toolchain image of the edition: internal tests
# (learn-test), app (learn-build), served by learn-run on port 8080.
ARG REGISTRY=ghcr.io/gmalnati
FROM ${REGISTRY}/learn-toolchain-node:2026.3
# The code runs as the unprivileged user of the toolchain image, never as root.
USER learn
WORKDIR /workspace
COPY --chown=learn:learn . .
RUN learn-test && learn-build
EXPOSE 8080
CMD ["learn-run"]

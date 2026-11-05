# syntax=docker/dockerfile:1
# Checks the files of ./${SRC} (starter or solution) without building them: the static checks of
# static.yaml (learn-check-static, ADR 033), with the rules of policy/. The report stays in
# the image (/reports/static), where learn check reads it.
ARG REGISTRY=ghcr.io/gmalnati
FROM ${REGISTRY}/learn-check-static:2026.6
# The user of the verifier, already the image's: declared so that scanners see a non-root user.
USER 10001:10001
COPY --chown=10001:10001 --from=exercise static.yaml /opt/learn/static.yaml
COPY --chown=10001:10001 --from=exercise policy /opt/learn/policy
# Copied, not mounted: a mounted file belongs to root, and one readable by its owner only (umask
# 077) could not be read by the verifier.
COPY --chown=10001:10001 . /workspace
RUN ["learn-check-static", "/opt/learn/static.yaml"]

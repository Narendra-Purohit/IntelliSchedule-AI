"""Structured logger utility for document intelligence service."""
import logging
import sys
import uuid

def get_logger(name: str = "doc-intel") -> logging.Logger:
    logger = logging.getLogger(name)
    if not logger.handlers:
        logger.setLevel(logging.INFO)
        handler = logging.StreamHandler(sys.stdout)
        formatter = logging.Formatter(
            fmt="%(asctime)s [%(levelname)s] [%(name)s] [trace_id=%(trace_id)s]: %(message)s",
            datefmt="%Y-%m-%d %H:%M:%S"
        )
        handler.setFormatter(formatter)
        logger.addHandler(handler)
    return logger

def generate_trace_id() -> str:
    return str(uuid.uuid4())[:8]

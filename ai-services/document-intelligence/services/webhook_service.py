"""Asynchronous webhook dispatcher service."""
import httpx
from schemas.webhook import WebhookNotification
from utils.logger import get_logger

logger = get_logger("webhook-dispatcher")

async def dispatch_webhook(target_url: str, payload: WebhookNotification) -> bool:
    try:
        async with httpx.AsyncClient(timeout=10.0) as client:
            response = await client.post(target_url, json=payload.model_dump())
            logger.info(f"Dispatched webhook to {target_url}, status={response.status_code}")
            return response.status_code in (200, 201, 202)
    except Exception as e:
        logger.error(f"Failed to dispatch webhook to {target_url}: {e}")
        return False

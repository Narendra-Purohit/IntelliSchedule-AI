"""HTTP client for fetching timetable context."""
import httpx
from config import settings

class TimetableQueryClient:
    def __init__(self, base_url: str = settings.timetable_service_url):
        self.base_url = base_url

    async def get_schedule(self) -> list:
        try:
            async with httpx.AsyncClient(timeout=5.0) as client:
                res = await client.get(f"{self.base_url}/api/v1/timetables")
                if res.status_code == 200:
                    return res.json()
        except Exception:
            pass
        return []

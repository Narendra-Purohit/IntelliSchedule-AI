// IntelliSchedule Client Dashboard Script
const API_BASE = "http://localhost:8080/api/v1";

async function loadTimetable() {
    const tbody = document.getElementById("timetableBody");
    try {
        const res = await fetch(`${API_BASE}/timetables`);
        if (!res.ok) throw new Error("Failed to load timetable");
        const data = await res.json();
        tbody.innerHTML = "";
        if (!data || data.length === 0) {
            tbody.innerHTML = "<tr><td colspan='7' class='text-center'>No schedules generated yet.</td></tr>";
            return;
        }
        data.forEach(item => {
            const tr = document.createElement("tr");
            tr.innerHTML = `
                <td>${item.dayOfWeek || '-'}</td>
                <td>${item.timeSlot ? item.timeSlot.startTime : '-'}</td>
                <td>${item.subject ? item.subject.name : '-'}</td>
                <td>${item.subject ? item.subject.code : '-'}</td>
                <td>${item.faculty ? item.faculty.name : '-'}</td>
                <td>${item.room ? item.room.roomNumber : '-'}</td>
                <td>${item.section ? item.section.name : '-'}</td>
            `;
            tbody.appendChild(tr);
        });
    } catch (e) {
        tbody.innerHTML = `<tr><td colspan='7' style='color:red;'>Error connecting to API Gateway (${e.message})</td></tr>`;
    }
}

document.getElementById("generateBtn").addEventListener("click", async () => {
    try {
        const res = await fetch(`${API_BASE}/timetables/generate`, { method: "POST" });
        if (res.ok) {
            alert("Timetable generated successfully!");
            loadTimetable();
        } else {
            alert("Generator returned status: " + res.status);
        }
    } catch (e) {
        alert("Failed to trigger generator: " + e.message);
    }
});

document.addEventListener("DOMContentLoaded", loadTimetable);

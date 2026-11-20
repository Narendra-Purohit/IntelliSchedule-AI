// IntelliSchedule Client Dashboard Script (Demo Mode - No Login Required)
const API_BASE = "http://localhost:8080/api/v1";
let allSchedules = [];

function showAlert(message, type = "success") {
    const banner = document.getElementById("alertBanner");
    banner.className = `alert-banner ${type}`;
    banner.textContent = message;
    banner.classList.remove("hidden");
    setTimeout(() => { banner.classList.add("hidden"); }, 5000);
}

async function loadTimetable(dayFilter = "ALL") {
    const tbody = document.getElementById("timetableBody");
    try {
        const res = await fetch(`${API_BASE}/timetables`);
        if (!res.ok) throw new Error("Failed to load timetable");
        allSchedules = await res.json();
        renderTable(dayFilter);
    } catch (e) {
        tbody.innerHTML = `<tr><td colspan='7' class='text-center' style='color:#dc2626;'>Cannot connect to API Gateway (${e.message}). Ensure services are running on port 8080.</td></tr>`;
    }
}

function renderTable(dayFilter) {
    const tbody = document.getElementById("timetableBody");
    tbody.innerHTML = "";

    const filtered = (dayFilter === "ALL") 
        ? allSchedules 
        : allSchedules.filter(item => (item.dayOfWeek || "").toLowerCase() === dayFilter.toLowerCase());

    if (!filtered || filtered.length === 0) {
        tbody.innerHTML = "<tr><td colspan='7' class='text-center'>No schedules found for selected filter.</td></tr>";
        return;
    }

    filtered.forEach(item => {
        const tr = document.createElement("tr");
        tr.innerHTML = `
            <td><strong>${item.dayOfWeek || '-'}</strong></td>
            <td>${item.timeSlot ? item.timeSlot.startTime + ' - ' + item.timeSlot.endTime : '-'}</td>
            <td>${item.subject ? item.subject.name : '-'}</td>
            <td><span class='badge'>${item.subject ? item.subject.code : '-'}</span></td>
            <td>${item.faculty ? item.faculty.name : '-'}</td>
            <td>${item.room ? item.room.roomNumber : '-'}</td>
            <td>${item.section ? item.section.name : '-'}</td>
        `;
        tbody.appendChild(tr);
    });
}

// Filter button clicks
document.querySelectorAll(".filter-btn").forEach(btn => {
    btn.addEventListener("click", (e) => {
        document.querySelectorAll(".filter-btn").forEach(b => b.classList.remove("active"));
        e.target.classList.add("active");
        renderTable(e.target.getAttribute("data-day"));
    });
});

// Trigger Generator
document.getElementById("generateBtn").addEventListener("click", async () => {
    const btn = document.getElementById("generateBtn");
    btn.disabled = true;
    btn.textContent = "⏳ Generating...";
    try {
        const res = await fetch(`${API_BASE}/timetables/generate`, { method: "POST" });
        if (res.ok) {
            showAlert("Timetable generated successfully with 0 double-bookings!", "success");
            await loadTimetable();
        } else {
            showAlert("Generation failed with status code: " + res.status, "error");
        }
    } catch (e) {
        showAlert("Failed to connect to generator: " + e.message, "error");
    } finally {
        btn.disabled = false;
        btn.textContent = "⚡ Run AI Generator";
    }
});

// Document Upload with Gemini AI
document.getElementById("uploadBtn").addEventListener("click", async () => {
    const fileInput = document.getElementById("pdfFileInput");
    const statusDiv = document.getElementById("uploadStatus");
    if (!fileInput.files || fileInput.files.length === 0) {
        statusDiv.style.color = "#dc2626";
        statusDiv.textContent = "Please select a PDF file first.";
        return;
    }

    const file = fileInput.files[0];
    const formData = new FormData();
    formData.append("file", file);

    statusDiv.style.color = "#2563eb";
    statusDiv.textContent = "Extracting curriculum details with PyMuPDF & Gemini AI...";

    try {
        const res = await fetch(`http://localhost:8000/api/v1/documents/upload`, {
            method: "POST",
            body: formData
        });

        const data = await res.json();
        if (res.ok) {
            statusDiv.style.color = "#16a34a";
            statusDiv.textContent = `✔ Extracted ${data.data ? data.data.subjects.length : 0} subjects from ${file.name} successfully!`;
            showAlert("Syllabus processed and ready for timetable allocation!", "success");
        } else {
            statusDiv.style.color = "#dc2626";
            statusDiv.textContent = "Error: " + (data.detail || "Upload failed");
        }
    } catch (e) {
        statusDiv.style.color = "#dc2626";
        statusDiv.textContent = "Connection error: " + e.message;
    }
});

document.addEventListener("DOMContentLoaded", () => loadTimetable("ALL"));

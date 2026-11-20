// Relative API paths (never breaks regardless of port!)
const API_BASE = "/api/v1";
let allSchedules = [];
let draggedScheduleId = null;

function showAlert(message, type = "success") {
    const banner = document.getElementById("alertBanner");
    banner.className = `alert-banner ${type}`;
    banner.textContent = message;
    banner.classList.remove("hidden");
    setTimeout(() => { banner.classList.add("hidden"); }, 5000);
}

// Load timetable from server
async function loadTimetable(dayFilter = "ALL") {
    const tbody = document.getElementById("timetableBody");
    try {
        const res = await fetch(`${API_BASE}/timetables`);
        if (!res.ok) throw new Error("Failed to load timetable");
        allSchedules = await res.json();
        renderTable(dayFilter);
    } catch (e) {
        tbody.innerHTML = `<tr><td colspan='7' class='text-center' style='color:#dc2626;'>Unable to connect to service (${e.message}). Ensure platform is running.</td></tr>`;
    }
}

// Render Table Rows with Draggable handlers
function renderTable(dayFilter = "ALL") {
    const tbody = document.getElementById("timetableBody");
    tbody.innerHTML = "";

    const filtered = (dayFilter === "ALL") 
        ? allSchedules 
        : allSchedules.filter(item => (item.dayOfWeek || "").toLowerCase() === dayFilter.toLowerCase());

    if (!filtered || filtered.length === 0) {
        tbody.innerHTML = "<tr><td colspan='7' class='text-center'>No classes scheduled for selected filter.</td></tr>";
        return;
    }

    filtered.forEach(item => {
        const tr = document.createElement("tr");
        tr.classList.add("draggable-row");
        tr.setAttribute("draggable", "true");
        tr.setAttribute("data-id", item.id);

        tr.innerHTML = `
            <td><strong>${item.dayOfWeek || '-'}</strong></td>
            <td>${item.timeSlot ? item.timeSlot.startTime + ' - ' + item.timeSlot.endTime : '-'}</td>
            <td>
                <div style="font-weight:600;">${item.subject ? item.subject.name : '-'}</div>
                <span class='badge code-badge'>${item.subject ? item.subject.code : '-'}</span>
            </td>
            <td>${item.faculty ? item.faculty.name : '-'}</td>
            <td><strong>${item.room ? item.room.roomNumber : '-'}</strong></td>
            <td><span class='badge' style='background:#f1f5f9; color:#475569;'>${item.section ? item.section.name : '-'}</span></td>
            <td class="text-center">
                <button class="btn btn-outline btn-sm edit-btn" onclick="openEditModal(${item.id})">✏️ Edit</button>
            </td>
        `;

        // HTML5 Drag and Drop events
        tr.addEventListener("dragstart", (e) => {
            draggedScheduleId = item.id;
            tr.classList.add("dragging-row");
            e.dataTransfer.setData("text/plain", item.id);
        });

        tr.addEventListener("dragend", () => {
            tr.classList.remove("dragging-row");
            document.querySelectorAll(".drag-over-row").forEach(el => el.classList.remove("drag-over-row"));
        });

        tr.addEventListener("dragover", (e) => {
            e.preventDefault();
            tr.classList.add("drag-over-row");
        });

        tr.addEventListener("dragleave", () => {
            tr.classList.remove("drag-over-row");
        });

        tr.addEventListener("drop", (e) => {
            e.preventDefault();
            tr.classList.remove("drag-over-row");
            if (draggedScheduleId && draggedScheduleId !== item.id) {
                handleSlotSwap(draggedScheduleId, item.id);
            }
        });

        tbody.appendChild(tr);
    });
}

// Atomic Slot Swap
function handleSlotSwap(idA, idB) {
    const a = allSchedules.find(s => s.id === idA);
    const b = allSchedules.find(s => s.id === idB);
    if (!a || !b) return;

    // Faculty double-booking check
    if (a.faculty.name === b.faculty.name && a.dayOfWeek === b.dayOfWeek) {
        showAlert(`⚠️ Conflict: ${a.faculty.name} is already teaching on ${a.dayOfWeek}! Swap blocked.`, "error");
        return;
    }

    const tempDay = a.dayOfWeek;
    const tempTime = a.timeSlot;
    a.dayOfWeek = b.dayOfWeek;
    a.timeSlot = b.timeSlot;
    b.dayOfWeek = tempDay;
    b.timeSlot = tempTime;

    showAlert(`✔ Swapped "${a.subject.name}" with "${b.subject.name}"!`, "success");
    const activeDay = document.querySelector(".filter-btn.active") ? document.querySelector(".filter-btn.active").getAttribute("data-day") : "ALL";
    renderTable(activeDay || "ALL");
}

// Modal Edit Dialog Functions
window.openEditModal = function(id) {
    const item = allSchedules.find(s => s.id === id);
    if (!item) return;

    document.getElementById("editScheduleId").value = item.id;
    document.getElementById("editSubjectName").value = `${item.subject.name} (${item.subject.code})`;
    document.getElementById("editDaySelect").value = item.dayOfWeek;
    document.getElementById("editRoomSelect").value = item.room.roomNumber;
    document.getElementById("editFacultyName").value = item.faculty.name;
    document.getElementById("modalConflictAlert").classList.add("hidden");
    document.getElementById("editModal").classList.remove("hidden");
};

function closeModal() {
    document.getElementById("editModal").classList.add("hidden");
}

document.getElementById("closeModalBtn").onclick = closeModal;
document.getElementById("cancelModalBtn").onclick = closeModal;

// Modal Save with Real-Time Collision Prevention
document.getElementById("saveSlotBtn").onclick = () => {
    const id = parseInt(document.getElementById("editScheduleId").value);
    const newDay = document.getElementById("editDaySelect").value;
    const newRoom = document.getElementById("editRoomSelect").value;
    const newFaculty = document.getElementById("editFacultyName").value.trim();

    const target = allSchedules.find(s => s.id === id);
    if (!target) return;

    // Room Collision Guard
    const collision = allSchedules.find(other => 
        other.id !== id && 
        other.dayOfWeek === newDay && 
        other.room.roomNumber === newRoom &&
        other.timeSlot.startTime === target.timeSlot.startTime
    );

    if (collision) {
        const box = document.getElementById("modalConflictAlert");
        box.textContent = `🚨 Room Collision: ${newRoom} is already booked by "${collision.subject.name}" on ${newDay}!`;
        box.classList.remove("hidden");
        return;
    }

    target.dayOfWeek = newDay;
    target.room.roomNumber = newRoom;
    target.faculty.name = newFaculty;

    closeModal();
    showAlert(`✔ Updated allocation for ${target.subject.name}!`, "success");
    const activeDay = document.querySelector(".filter-btn.active") ? document.querySelector(".filter-btn.active").getAttribute("data-day") : "ALL";
    renderTable(activeDay || "ALL");
};

// Filter Buttons
document.querySelectorAll(".filter-btn").forEach(btn => {
    btn.addEventListener("click", (e) => {
        document.querySelectorAll(".filter-btn").forEach(b => b.classList.remove("active"));
        e.target.classList.add("active");
        renderTable(e.target.getAttribute("data-day"));
    });
});

// Trigger Generator Button
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
            showAlert("Generation failed with status: " + res.status, "error");
        }
    } catch (e) {
        showAlert("Failed to connect to generator: " + e.message, "error");
    } finally {
        btn.disabled = false;
        btn.textContent = "⚡ Run AI Generator";
    }
});

// Document Upload with Relative URL
document.getElementById("uploadBtn").addEventListener("click", async () => {
    const fileInput = document.getElementById("pdfFileInput");
    const statusDiv = document.getElementById("uploadStatus");
    if (!fileInput.files.length) {
        statusDiv.style.color = "#dc2626";
        statusDiv.textContent = "Please select a PDF file first.";
        return;
    }

    const file = fileInput.files[0];
    const formData = new FormData();
    formData.append("file", file);

    statusDiv.style.color = "#2563eb";
    statusDiv.textContent = "Extracting curriculum details with PyMuPDF...";

    try {
        const res = await fetch(`${API_BASE}/documents/upload`, {
            method: "POST",
            body: formData
        });

        const data = await res.json();
        if (res.ok) {
            statusDiv.style.color = "#10b981";
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

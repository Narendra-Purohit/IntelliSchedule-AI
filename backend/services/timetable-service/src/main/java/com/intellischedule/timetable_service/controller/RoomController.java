package com.intellischedule.timetable_service.controller;

import com.intellischedule.timetable_service.entity.Room;
import com.intellischedule.timetable_service.service.RoomService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping
    public ResponseEntity<Room> createRoom(
            @RequestBody Room room
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(roomService.createRoom(room));
    }

    @GetMapping
    public ResponseEntity<List<Room>> getAllRooms() {
        return ResponseEntity.ok(
                roomService.getAllRooms()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Room> getRoomById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                roomService.getRoomById(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Room> updateRoom(
            @PathVariable Long id,
            @RequestBody Room room
    ) {
        return ResponseEntity.ok(
                roomService.updateRoom(id, room)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(
            @PathVariable Long id
    ) {
        roomService.deleteRoom(id);
        return ResponseEntity.noContent().build();
    }
}
package com.intellischedule.timetable_service.service;

import com.intellischedule.timetable_service.entity.Room;
import com.intellischedule.timetable_service.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public Room createRoom(Room room) {

        if (roomRepository.existsByRoomNumber(room.getRoomNumber())) {
            throw new IllegalArgumentException(
                    "Room number already exists: " + room.getRoomNumber()
            );
        }

        return roomRepository.save(room);
    }

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public Room getRoomById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Room not found: " + id)
                );
    }

    public Room updateRoom(Long id, Room updatedRoom) {

        Room room = getRoomById(id);

        room.setRoomNumber(updatedRoom.getRoomNumber());
        room.setRoomType(updatedRoom.getRoomType());
        room.setCapacity(updatedRoom.getCapacity());

        return roomRepository.save(room);
    }

    public void deleteRoom(Long id) {
        Room room = getRoomById(id);
        roomRepository.delete(room);
    }
}
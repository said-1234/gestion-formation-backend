package com.projet.formation.controllers;

import com.projet.formation.dto.ParticipantDto;
import com.projet.formation.dto.AjoutResponse;
import com.projet.formation.mapper.ObjectMapperUtils;
import com.projet.formation.models.Participant;
import com.projet.formation.models.Session;
import com.projet.formation.repository.ParticipantRepository;
import com.projet.formation.repository.SessionRepository;
import com.projet.formation.services.ParticipantService;
import com.projet.formation.services.RoleService;
import com.projet.formation.services.SessionService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;


@RestController
@RequestMapping("/api/participants")
public class ParticipantsController {

    @Autowired
    private ParticipantService participantService;
    @Autowired
    private ParticipantRepository participantRepository;
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private SessionService sessionService;
    ModelMapper modelMapper = new ModelMapper();
    @Autowired
    private RoleService roleService;

    @GetMapping("/participants")
    public ResponseEntity <List<ParticipantDto>> getAllParticipants() {
        List<Participant> listOfParticipant =participantRepository.findAll();
        List<ParticipantDto> listOfParticipantDto = ObjectMapperUtils.mapAll(participantRepository.findAll(), ParticipantDto.class);
        Collections.reverse(listOfParticipantDto);
        return new ResponseEntity<>(listOfParticipantDto, HttpStatus.OK);
    }

    @PostMapping(value = "/addParticipant")
    public ResponseEntity<ParticipantDto> addOrUpdateParticipant(@Valid @RequestBody ParticipantDto participantDto) {

        Participant participant = ObjectMapperUtils.map(participantDto, Participant.class);
        for(Session s: participant.getSession()) {
            Session ss = sessionRepository.getOne(s.getSessionId());
            ss.getParticipants().add(participant);
            sessionRepository.save(ss);
        }
        ParticipantDto participantDto1 = ObjectMapperUtils.map(participantRepository.save(participant),ParticipantDto.class);
        return new ResponseEntity<>(participantDto1, HttpStatus.OK);
    }


//    @GetMapping("/getUserById")
//    public ResponseEntity<UserDto> getUserById(@RequestParam(name = "id", required = true) Long id) {
//        UserDto userDTO = modelMapper.map(userRepository.getOne(id), UserDto.class);
//        return new ResponseEntity<>(userDTO, HttpStatus.OK);
//    }

    @PostMapping("/deleteParticipant")
    public ResponseEntity deleteParticipant(@RequestBody long id) {
        AjoutResponse ajoutResponse = new AjoutResponse();
        try{
            ajoutResponse = participantService.deleteById( id);

        }catch (Exception e){
            ajoutResponse.setMessage(e.getMessage());
        }
        return new ResponseEntity(ajoutResponse,HttpStatus.OK);
    }

}

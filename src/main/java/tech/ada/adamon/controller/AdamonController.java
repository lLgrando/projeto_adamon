package tech.ada.adamon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.ada.adamon.model.Adamon;
import tech.ada.adamon.service.AdamonService;
import tech.ada.adamon.service.JogadorService;

import java.util.List;

@RestController
@RequestMapping("/adamon")
public class AdamonController {

    @Autowired
    private AdamonService adamonService;

    @Autowired
    private JogadorService jogadorService;

    @GetMapping
    public ResponseEntity<List<Adamon>> recuperarTodosAdamons() {
        return new ResponseEntity<>(adamonService.recuperarTodosAdamons(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Adamon> criarNovoAdamon(@RequestBody Adamon adamon) {
        return new ResponseEntity(adamonService.criarNovoAdamon(adamon), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Adamon> encontraAdamonPorId(@PathVariable("id") Long id) {
        return new ResponseEntity<>(adamonService.encontrarAdamonPorId(id), HttpStatus.OK);
    }

    /* 
    @GetMapping("/duelar")
    public Long duelar(@RequestParam("id1") Long id1, @RequestParam("id2") Long id2){
        return id1 + id2; 
    }
    */

    @PostMapping("/duelar")
    public List<List<String>> duelar(@RequestParam Long adamonId1, @RequestParam Long adamonId2){
        return jogadorService.batalhar(adamonId1, adamonId2);
    }

}
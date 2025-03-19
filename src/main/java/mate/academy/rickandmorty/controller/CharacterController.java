package mate.academy.rickandmorty.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import mate.academy.rickandmorty.model.Character;
import mate.academy.rickandmorty.service.CharacterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Tag(name = "Character API", description = "Endpoints for managing Rick & Morty characters")
public class CharacterController {
    private final CharacterService characterService;

    @Autowired
    public CharacterController(CharacterService characterService) {
        this.characterService = characterService;
    }

    @Operation(summary = "Get a random character")
    @GetMapping("/character/random")
    public Character getRandomCharacter() {
        return characterService.getRandomCharacter();
    }

    @Operation(summary = "Search characters by name")
    @GetMapping("/characters")
    public List<Character> searchCharacters(@RequestParam("name") String name) {
        return characterService.searchCharactersByName(name);
    }
}

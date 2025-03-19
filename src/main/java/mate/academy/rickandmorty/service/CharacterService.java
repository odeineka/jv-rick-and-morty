package mate.academy.rickandmorty.service;

import jakarta.annotation.PostConstruct;
import java.util.List;
import mate.academy.rickandmorty.model.Character;
import mate.academy.rickandmorty.repository.CharacterRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CharacterService {
    private static final String EXTERNAL_API_URL = "https://rickandmortyapi.com/api/character";
    private final CharacterRepository characterRepository;
    private final RestTemplate restTemplate;

    public CharacterService(CharacterRepository characterRepository, RestTemplate restTemplate) {
        this.characterRepository = characterRepository;
        this.restTemplate = restTemplate;
    }

    @PostConstruct
    public void loadCharacters() {

        String url = EXTERNAL_API_URL;
        do {
            ExternalApiResponse response = restTemplate.getForObject(url,
                    ExternalApiResponse.class);
            if (response != null && response.getResults() != null) {
                for (ExternalCharacter ec : response.getResults()) {
                    Character character = new Character();
                    character.setExternalId(String.valueOf(ec.getId()));
                    character.setName(ec.getName());
                    character.setStatus(ec.getStatus());
                    character.setGender(ec.getGender());
                    characterRepository.save(character);
                }
                url = response.getInfo().getNext();
            } else {
                url = null;
            }
        } while (url != null);
    }

    public List<Character> searchCharactersByName(String name) {
        return characterRepository.findByNameContainingIgnoreCase(name);
    }

    public Character getRandomCharacter() {
        List<Character> characters = characterRepository.findAll();
        if (characters.isEmpty()) {
            return null;
        }
        int randomIndex = (int) (Math.random() * characters.size());
        return characters.get(randomIndex);
    }

    public static class ExternalApiResponse {
        private Info info;
        private List<ExternalCharacter> results;

        public Info getInfo() {
            return info;
        }

        public void setInfo(Info info) {
            this.info = info;
        }

        public List<ExternalCharacter> getResults() {
            return results;
        }

        public void setResults(List<ExternalCharacter> results) {
            this.results = results;
        }
    }

    public static class Info {
        private int count;
        private int pages;
        private String next;
        private String prev;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getPages() {
            return pages;
        }

        public void setPages(int pages) {
            this.pages = pages;
        }

        public String getNext() {
            return next;
        }

        public void setNext(String next) {
            this.next = next;
        }

        public String getPrev() {
            return prev;
        }

        public void setPrev(String prev) {
            this.prev = prev;
        }
    }

    public static class ExternalCharacter {
        private int id;
        private String name;
        private String status;
        private String gender;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

    }
}

package prizehoggers.songmaker.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import prizehoggers.songmaker.data.PlayListData;
import prizehoggers.songmaker.service.PlayListGen;

import java.util.List;

@RestController
@RequestMapping("/playList/")
public class PlayListController {
    @GetMapping("/query")
    public List<?> query(@RequestParam String mood) {
        return PlayListGen.getPlayList(mood)
                .stream()
                .map(i -> new PlayListData(i.getName(), i.getExternalUrls()
                        .get("spotify"), i.getImages()[0].toString()))
                .toList();
    }
}

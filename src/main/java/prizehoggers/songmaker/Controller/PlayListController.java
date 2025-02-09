package prizehoggers.songmaker.Controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import prizehoggers.songmaker.data.PlayListData;
import prizehoggers.songmaker.service.PlayListGen;

@RestController
@RequestMapping("/playList/")
public class PlayListController {
    @GetMapping("/query")
    public List<?> query(@RequestParam String mood) {
        List<PlayListData> ans = new ArrayList<>(PlayListGen.getPlayList(mood)
                .stream()
                .map(i -> new PlayListData(i.getName(), i.getExternalUrls().get("spotify"), i.getImages()[0].getUrl()))
                .toList());
        int times = Math.min(3, ans.size());
        List<PlayListData> res = new ArrayList<>();
        for (int i = 0; i < times; i++) {
            // chose 3 random songs
            int index = (int) (Math.random() * ans.size());
            res.add(ans.remove(index));
        }
        return res;
    }
}

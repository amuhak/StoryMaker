package prizehoggers.songmaker.service.editor;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class VideoEditorConverter {
    public record Status(AtomicBoolean doneInternal, AtomicBoolean done, AtomicInteger parts) {
    }
}

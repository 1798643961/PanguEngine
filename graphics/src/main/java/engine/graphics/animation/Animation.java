package engine.graphics.animation;

import com.github.mouse0w0.observable.value.MutableValue;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class Animation<T> {
    private MutableValue<T> animatingProperty;
    private Map<Double, AnimationKeyframe<T>> keyframes = new HashMap<>();

    public Animation(MutableValue<T> prop) {
        this.animatingProperty = prop;
    }

    public double getDuration() {
        return keyframes.keySet().stream().max(Comparator.naturalOrder()).orElse(0.0);
    }

    public MutableValue<T> getAnimatingProperty() {
        return animatingProperty;
    }

    public void addKeyframe(double timestamp, AnimationKeyframe<T> keyframe) {
        keyframes.put(timestamp, keyframe);
        keyframe.setTimestamp(timestamp);
    }

    public AnimationKeyframe<T> getNextFrame(double progress) {
        if (keyframes.size() == 0) return null;
        var doubleStream = keyframes.keySet().parallelStream().sorted(Comparator.naturalOrder()).filter(d -> progress <= d);
        var nextKeyframe = doubleStream.findFirst().orElse(getDuration());
        return keyframes.get(nextKeyframe);
    }

    public AnimationKeyframe<T> getPreviousFrame(double progress){
        if(keyframes.size() == 0) return null;
        var keyframe = keyframes.keySet().parallelStream().filter(d -> progress > d).max(Comparator.naturalOrder()).orElse(0.0);
        if(keyframe == 0.0 && !keyframes.containsKey(0.0)){ //When time=0 has not set a keyframe, use the first keyframe instead
            return keyframes.entrySet().stream().min(Comparator.comparingDouble(Map.Entry::getKey)).get().getValue();
        }
        return keyframes.get(keyframe);
    }
}

package org.spigotmc.timings;

import static org.spigotmc.timings.TimingsManager.*;

public class FullServerTickHandler extends TimingHandler {
    static final TimingIdentifier IDENTITY = new TimingIdentifier("Minecraft", "Full Server Tick", null, false);
    final TimingData minuteData;
    FullServerTickHandler() {
        super(IDENTITY);
        minuteData = new TimingData(id);

        TIMING_MAP.put(IDENTITY, this);
    }

    @Override
    public void startTiming() {
        if (TimingsManager.needsFullReset) {
            TimingsManager.resetTimings();
        } else if (TimingsManager.needsRecheckEnabled) {
            TimingsManager.recheckEnabled();
        }
        super.startTiming();
    }

    @Override
    public void stopTiming() {
        super.stopTiming();
        if (!enabled) {
            return;
        }

        long start = System.nanoTime();
        TimingsManager.tick();
        long diff = System.nanoTime() - start;
        CURRENT = TIMINGS_TICK;
        TIMINGS_TICK.addDiff(diff);
        // addDiff for TIMINGS_TICK incremented this, bring it back down to 1 per tick.
        record.curTickCount--;
        minuteData.curTickTotal = record.curTickTotal;
        minuteData.curTickCount = 1;
        boolean violated = isViolated();
        minuteData.processTick(violated);
        TIMINGS_TICK.processTick(violated);
        processTick(violated);


        if (TimingHistory.timedTicks % 1200 == 0) {
            MINUTE_REPORTS.add(new TimingHistory.MinuteReport());
            TimingHistory.resetTicks(false);
            minuteData.reset();
        }
        if (TimingHistory.timedTicks % Timings.getHistoryInterval() == 0) {
            TimingsManager.HISTORY.add(new TimingHistory());
            TimingsManager.resetTimings();
        }
    }

    boolean isViolated() {
        return record.curTickTotal > 50000000;
    }
}

/*
 * MIT License
 *
 * Copyright (c) 2023 Tohjuler
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package me.tohjuler.cmdmanager.utils;

import org.bukkit.entity.Entity;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Cooldown {

    private final Map<Object, Long> cooldowns = new HashMap<>();

    private Object getUuid(Object obj) {
        if (obj instanceof Entity)
            return ((Entity) obj).getUniqueId();
        return obj;
    }

    /**
     * Sets a cooldown for an object
     *
     * @param obj The object to set the cooldown for
     * @param duration The duration of the cooldown
     */
    public void setCooldown(Object obj, long duration) {
        cooldowns.put(getUuid(obj), System.currentTimeMillis() + duration);
    }

    /**
     * Sets a cooldown for an object with a time unit
     *
     * @param obj The object to set the cooldown for
     * @param duration The duration of the cooldown
     * @param timeUnit The time unit of the duration
     */
    public void setCooldown(Object obj, long duration, TimeUnit timeUnit) {
        setCooldown(obj, timeUnit.toMillis(duration));
    }

    /**
     * Removes a cooldown from an object
     *
     * @param obj The object to remove the cooldown from
     */
    public void removeCooldown(Object obj) {
        cooldowns.remove(getUuid(obj));
    }

    /**
     * Checks if an object has a cooldown
     *
     * @param obj The object to check
     * @return Whether the object has a cooldown
     */
    public boolean hasCooldown(Object obj) {
        return getRemainingTime(obj) > 0;
    }

    /**
     * Gets the remaining time of an object's cooldown
     *
     * @param obj The object to get the remaining time for
     * @return The remaining time in milliseconds
     */
    public long getRemainingTime(Object obj) {
        return getRemainingTime(obj, TimeUnit.MILLISECONDS);
    }

    /**
     * Gets the remaining time of an object's cooldown
     *
     * @param obj The object to get the remaining time for
     * @param timeUnit The time unit to get the remaining time in
     * @return The remaining time in the specified time unit
     */
    public long getRemainingTime(Object obj, TimeUnit timeUnit) {
        long end = cooldowns.getOrDefault(getUuid(obj), 0L);
        return end == 0 ? 0 : timeUnit.convert(end - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }

    /**
     * Adds time to an object's cooldown
     *
     * @param obj The object to add time to
     * @param time The time to add
     */
    public void addTimeToCooldown(Object obj, long time) {
        addTimeToCooldown(obj, time, TimeUnit.MILLISECONDS);
    }

    /**
     * Adds time to an object's cooldown
     *
     * @param obj The object to add time to
     * @param time The time to add
     * @param timeUnit The time unit of the time
     */
    public void addTimeToCooldown(Object obj, long time, TimeUnit timeUnit) {
        setCooldown(obj, getRemainingTime(obj, timeUnit) + time, timeUnit);
    }

    /**
     * Removes time from an object's cooldown
     *
     * @param obj The object to remove time from
     * @param time The time to remove
     */
    public void removeTimeFromCooldown(Object obj, long time) {
        removeTimeFromCooldown(obj, time, TimeUnit.MILLISECONDS);
    }

    /**
     * Removes time from an object's cooldown
     *
     * @param obj The object to remove time from
     * @param time The time to remove
     * @param timeUnit The time unit of the time
     */
    public void removeTimeFromCooldown(Object obj, long time, TimeUnit timeUnit) {
        setCooldown(obj, getRemainingTime(obj, timeUnit) - time, timeUnit);
    }
}

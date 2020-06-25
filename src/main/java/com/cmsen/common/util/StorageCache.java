/**
 * +---------------------------------------------------------
 * | Author Jared.Yan<yanhuaiwen@163.com>
 * +---------------------------------------------------------
 * | Copyright (c) http://cmsen.com All rights reserved.
 * +---------------------------------------------------------
 */
package com.cmsen.common.util;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author jared.Yan (yanhuaiwen@163.com)
 */
public class StorageCache {
    /**
     * 默认缓存有效时长
     */
    private static final int DEFAULT_TIMEOUT = 5000; // ms
    private static final Map<String, Object> memory;
    private static final Timer timer;

    static {
        timer = new Timer();
        memory = new LRUCache<String, Object>();
    }

    private StorageCache() {
    }

    static class LRUCache<K, V> extends LinkedHashMap<K, V> {
        /**
         * 读写锁
         */
        private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        private final Lock readLock = readWriteLock.readLock();
        private final Lock writeLock = readWriteLock.writeLock();

        /**
         * 初始缓存容量
         */
        private static final int INITIAL_CAPACITY = 1 << 4;

        /**
         * 最大缓存容量
         */
        private static final int MAX_CAPACITY = 1 << 30;

        /**
         * 加载因子
         */
        private static final float LOAD_FACTOR = 0.75f;

        public LRUCache() {
            super(INITIAL_CAPACITY, LOAD_FACTOR);
        }

        public LRUCache(int initialCapacity) {
            super(initialCapacity, LOAD_FACTOR);
        }

        public V get(String k) {
            readLock.lock();
            try {
                return super.get(k);
            } finally {
                writeLock.unlock();
            }
        }

        public V put(K key, V value) {
            writeLock.lock();
            try {
                return super.put(key, value);
            } finally {
                writeLock.unlock();
            }
        }

        public void putAll(Map<? extends K, ? extends V> data) {
            writeLock.lock();
            try {
                super.putAll(data);
            } finally {
                writeLock.unlock();
            }
        }

        public V remove(Object key) {
            writeLock.lock();
            try {
                return super.remove(key);
            } finally {
                writeLock.unlock();
            }
        }


        public boolean containKey(K key) {
            readLock.lock();
            try {
                return super.containsKey(key);
            } finally {
                readLock.unlock();
            }
        }

        public int size() {
            readLock.lock();
            try {
                return super.size();
            } finally {
                readLock.unlock();
            }
        }


        public void clear() {
            writeLock.lock();
            try {
                super.clear();
            } finally {
                writeLock.unlock();
            }
        }

        /**
         * 缓存大小超过默认大小则移除最先加入节点
         */
        @Override
        protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
            return size() > MAX_CAPACITY;
        }
    }

    /**
     * 清除缓存任务类
     */
    static class CleanWorkerTask extends TimerTask {

        private String key;

        public CleanWorkerTask(String key) {
            this.key = key;
        }

        @Override
        public void run() {
            System.out.println("Memory cache erase: " + key);
            StorageCache.remove(key);
        }
    }

    /**
     * 写入缓存
     *
     * @param key   键名
     * @param value 缓存值
     */
    public static void write(String key, Object value) {
        memory.put(key, value);
        timer.schedule(new CleanWorkerTask(key), DEFAULT_TIMEOUT);

    }

    /**
     * 写入缓存
     *
     * @param key     键名
     * @param value   缓存值
     * @param timeout 有效时长 ms
     */
    public static void write(String key, Object value, int timeout) {
        memory.put(key, value);
        timer.schedule(new CleanWorkerTask(key), timeout);
    }

    /**
     * 写入缓存
     *
     * @param key        键名
     * @param value      缓存值
     * @param expireTime 过期时间
     */
    public static void write(String key, Object value, Date expireTime) {
        memory.put(key, value);
        timer.schedule(new CleanWorkerTask(key), expireTime);
    }


    /**
     * 批量写入缓存
     *
     * @param data 数据
     */
    public static void writeAll(Map<String, Object> data) {
        writeAll(data, DEFAULT_TIMEOUT);
    }

    /**
     * 批量写入缓存
     *
     * @param data    数据
     * @param timeout 有效时长 ms
     */
    public static void writeAll(Map<String, Object> data, int timeout) {
        memory.putAll(data);
        for (String key : data.keySet()) {
            timer.schedule(new CleanWorkerTask(key), timeout);
        }
    }

    /**
     * 批量写入缓存
     *
     * @param data       数据
     * @param expireTime 过期时间
     */
    public static void writeAll(Map<String, Object> data, Date expireTime) {
        memory.putAll(data);
        for (String key : data.keySet()) {
            timer.schedule(new CleanWorkerTask(key), expireTime);
        }
    }

    /**
     * 读取缓存
     *
     * @param key 键名
     * @return
     */
    public static Object read(String key) {
        return memory.get(key);
    }

    /**
     * 判断缓存是否包含key
     *
     * @param key 键名
     * @return
     */
    public static boolean containsKey(String key) {
        return memory.containsKey(key);
    }

    /**
     * 删除缓存
     *
     * @param key
     */
    public static void remove(String key) {
        memory.remove(key);
    }

    /**
     * 返回缓存大小
     *
     * @return
     */
    public static int size() {
        return memory.size();
    }

    /**
     * 清除所有缓存
     */
    public static void clear() {
        if (size() > 0) {
            memory.clear();
        }
        timer.cancel();
    }

    /**
     * 缓存版本
     */
    public static String version() {
        return "CmSen Storage Memory Cache version:V0.1.0 RC";
    }
}

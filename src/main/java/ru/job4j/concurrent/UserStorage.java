package ru.job4j.concurrent;

import net.jcip.annotations.ThreadSafe;

import java.util.concurrent.ConcurrentHashMap;

@ThreadSafe
public class UserStorage {

    private final ConcurrentHashMap<Integer, User> users = new ConcurrentHashMap<>();

    public synchronized boolean add(User user) {
        return users.putIfAbsent(user.getId(), user) == null;
    }

    public synchronized boolean update(User user) {
        return users.replace(user.getId(), users.get(user.getId()), user);
    }

    public synchronized boolean delete(User user) {
        return users.remove(user.getId(), user);
    }

    public synchronized boolean transfer(int fromId, int toId, int amount) {
        User sender = users.get(fromId);
        User recipient = users.get(toId);
        boolean result = (sender != null && sender.getAmount() >= amount) && recipient != null;
        if (result) {
            sender.setAmount(sender.getAmount() - amount);
            recipient.setAmount(recipient.getAmount() + amount);
        }
        return result;
    }

}

package ru.job4j.concurrent;

import net.jcip.annotations.ThreadSafe;

import java.util.concurrent.ConcurrentHashMap;

@ThreadSafe
public class UserStorage {

    private final ConcurrentHashMap<Integer, User> users = new ConcurrentHashMap<>();

    public synchronized boolean add(User user) {
        boolean result = users.get(user.getId()) == null;
        if (result) {
            users.put(user.getId(), user);
        }
        return result;
    }

    public synchronized boolean update(User user) {
        boolean result = users.get(user.getId()) != null;
        if (result) {
            users.put(user.getId(), user);
        }
        return result;
    }

    public synchronized boolean delete(User user) {
        boolean result = users.get(user.getId()) != null;
        if (result) {
            users.remove(user.getId());
        }
        return result;
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

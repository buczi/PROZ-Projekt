package mvc.event;

import mvc.exception.CriticalException;

public interface CreateThread {
    void createThread();

    void reportException(CriticalException exception);
}

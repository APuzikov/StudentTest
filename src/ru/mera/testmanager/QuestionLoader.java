package ru.mera.testmanager;

import java.io.IOException;
import java.util.List;

public interface QuestionLoader {

    List<Question> load() throws  LoadException;
}

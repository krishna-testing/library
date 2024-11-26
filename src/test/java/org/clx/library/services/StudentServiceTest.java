package org.clx.library.services;

import org.clx.library.repositories.CardRepository;
import org.clx.library.repositories.StudentRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;

class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private CardRepository cardRepository;

    @InjectMocks
    private StudentService studentService;

    @InjectMocks
    private CardService cardService;




}

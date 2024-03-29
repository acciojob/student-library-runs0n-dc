package com.example.library.studentlibrary.services;

import com.example.library.studentlibrary.models.Student;
import com.example.library.studentlibrary.models.Card;
import com.example.library.studentlibrary.models.CardStatus;
import com.example.library.studentlibrary.models.Card;
import com.example.library.studentlibrary.models.CardStatus;
import com.example.library.studentlibrary.models.Student;
import com.example.library.studentlibrary.repositories.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.example.library.studentlibrary.models.CardStatus.ACTIVATED;


@Service
public class CardService {


    @Autowired
    CardRepository cardRepository3;

    public Card createAndReturn(Student student){
        Card card = new Card();
        card.setId(student.getId());
        card.setCreatedOn(new Date());
       // card.setCardStatus(ACTIVATED);
        cardRepository3.save(card);
        return card;
    }

    public void deactivateCard(int student_id){
        cardRepository3.deactivateCard(student_id, CardStatus.DEACTIVATED.toString());
    }
}
package com.example.library.studentlibrary.services;

import com.example.library.studentlibrary.models.Book;
import com.example.library.studentlibrary.models.Card;
import com.example.library.studentlibrary.models.Transaction;
import com.example.library.studentlibrary.models.TransactionStatus;
import com.example.library.studentlibrary.models.Book;
import com.example.library.studentlibrary.models.Card;
import com.example.library.studentlibrary.models.Transaction;
import com.example.library.studentlibrary.repositories.BookRepository;
import com.example.library.studentlibrary.repositories.CardRepository;
import com.example.library.studentlibrary.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static com.example.library.studentlibrary.models.CardStatus.ACTIVATED;
import static com.example.library.studentlibrary.models.CardStatus.DEACTIVATED;
import static com.example.library.studentlibrary.models.TransactionStatus.SUCCESSFUL;

@Service
public class TransactionService {

    @Autowired
    BookRepository bookRepository5;

    @Autowired
    CardRepository cardRepository5;

    @Autowired
    TransactionRepository transactionRepository5;

    @Value("${books.max_allowed}")
    public int max_allowed_books;

    @Value("${books.max_allowed_days}")
    public int getMax_allowed_days;

    @Value("${books.fine.per_day}")
    public int fine_per_day;

    public String issueBook(int cardId, int bookId) throws Exception {
        HashMap<Integer,Integer>  hm=new HashMap<>();
        if(hm.size()<=0){
            hm.put(cardId,1);
        }
        Card card=cardRepository5.findById(cardId).get();

        for(int i=0;i<hm.size();i++){
            if(hm.containsKey(cardId)){
                hm.put(cardId,hm.get(cardId)+1);
                if(hm.get(cardId)>3){
                    ((Card) card).setCardStatus(DEACTIVATED);
                    cardRepository5.save(card);
                    throw new Error("Book limit has reached for this card");
                }
            }else{
                hm.put(cardId,1);
            }
        }





        //check whether bookId and cardId already exist
        //conditions required for successful transaction of issue book:
        //1. book is present and available
        // If it fails: throw new Exception("Book is either unavailable or not present");
        //2. card is present and activated
        // If it fails: throw new Exception("Card is invalid");
        //3. number of books issued against the card is strictly less than max_allowed_books
        // If it fails: throw new Exception("Book limit has reached for this card");
        //If the transaction is successful, save the transaction to the list of transactions and return the id

        //Note that the error message should match exactly in all cases
        if(cardRepository5.existsById(cardId) && bookRepository5.existsById(bookId)){
            Book book=bookRepository5.findById(bookId).get();


            if(((Book) book).isAvailable()==false){
                throw new Error("Book is either unavailable or not present");
            }
            if(card.getCardStatus()==DEACTIVATED){
                throw new Error("Card is invalid");
            }
            if(card.getBooks().size()>max_allowed_books){
                card.setCardStatus(DEACTIVATED);
                throw new Error("Book limit has reached for this card");
            }

        }
        Book book=bookRepository5.findById(bookId).get();



        Transaction transaction=new Transaction();
        transaction.setFineAmount(0);
        transaction.setTransactionId(UUID.randomUUID().toString());
        transaction.setBook(book);
        transaction.setCard(card);
        transaction.setIssueOperation(true);
        transaction.setTransactionDate(new Date());
        transaction.setTransactionStatus(SUCCESSFUL);

        transactionRepository5.save(transaction);

        book.getTransactions().add(transaction);


        book.setAvailable(false);
        return transaction.getTransactionId();

        //return "Transition failed." ;//return transactionId instead
    }

    public Transaction returnBook(int cardId, int bookId) throws Exception{

        List<Transaction> transactions = transactionRepository5.find(cardId, bookId, SUCCESSFUL, true);
        Transaction transaction = transactions.get(transactions.size()-1);

        Date d1=transaction.getTransactionDate();
        Date d2=new Date();
        long diff=d2.getTime()- d1.getTime();
        int dif=(int) diff;
        if(dif>15){
            fine_per_day=(fine_per_day)*(dif-15);
        }
        Book book=bookRepository5.findById(bookId).get();
        book.setAvailable(true);
        bookRepository5.save(book);

        Card card=cardRepository5.findById(cardId).get();
        card.setCardStatus(ACTIVATED);



        //for the given transaction calculate the fine amount considering the book has been returned exactly when this function is called
        //make the book available for other users
        //make a new transaction for return book which contains the fine amount as well

        //transactionRepository5.delete(transaction);

        Transaction returnBookTransaction  = new Transaction();

        returnBookTransaction.setId(transaction.getId());
        returnBookTransaction.setTransactionId(transaction.getTransactionId());
        returnBookTransaction.setCard(transaction.getCard());
        returnBookTransaction.setBook(transaction.getBook());
        returnBookTransaction.setIssueOperation(transaction.isIssueOperation());//check later
        returnBookTransaction.setTransactionStatus(transaction.getTransactionStatus());
        returnBookTransaction.setTransactionDate(transaction.getTransactionDate());

        return returnBookTransaction; //return the transaction after updating all details
    }
}
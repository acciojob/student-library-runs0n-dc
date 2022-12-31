package com.example.library.studentlibrary.services;

import com.example.library.studentlibrary.models.Author;
import com.example.library.studentlibrary.models.Book;
import com.example.library.studentlibrary.models.Card;
import com.example.library.studentlibrary.repositories.AuthorRepository;
import com.example.library.studentlibrary.repositories.BookRepository;
import com.example.library.studentlibrary.repositories.CardRepository;
import com.example.library.studentlibrary.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class BookService {


    @Autowired
    BookRepository bookRepository2;
    @Autowired
    AuthorRepository authorRepository;

    @Autowired
    CardRepository cardRepository;

    @Autowired
    TransactionRepository transactionRepository;

    public void createBook(Book book){
        Author author= authorRepository.findById(book.getAuthor().getId()).get();
        Card card=cardRepository.findById(book.getCard().getId()).get();
        book.setAuthor(author);
        book.setAvailable(true);
        book.setCard(card);
        bookRepository2.save(book);
    }

    public List<Book> getBooks(String genre, boolean available, String author){
        List<Book> books=new ArrayList<>();
        if(genre!=null && available==true){
            books.add((Book) bookRepository2.findBooksByGenre(genre,true));
          //  books.add((Book) bookRepository2.findBooksByAuthor(author,true));
            books.add((Book) bookRepository2.findBooksByGenreAuthor(genre,null,true));
        }else if(genre!=null && author!=null && available==false){
            books.add((Book) bookRepository2.findBooksByGenre(genre,false));
           // books.add((Book) bookRepository2.findBooksByAuthor(author,false));
            books.add((Book) bookRepository2.findByAvailability(false));
        }
        //List<Book> books = null; //find the elements of the list by yourself
        return books;
    }
}
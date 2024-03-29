package com.example.library.studentlibrary.controller;

import com.example.library.studentlibrary.models.Book;
import com.example.library.studentlibrary.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//Add required annotations

@RestController
@RequestMapping("book")
public class BookController {

    @Autowired
    BookService bookService;
    @PostMapping("createBook")
    public  ResponseEntity<String> createBook(@RequestBody() Book book){
        bookService.createBook(book);
        return new ResponseEntity<>("Success",HttpStatus.CREATED);
    }

    @GetMapping("getBooks")
    public ResponseEntity getBooks(@RequestParam(value = "genre", required = false) String genre,
                                   @RequestParam(value = "available", required = false, defaultValue = "false") boolean available,
                                   @RequestParam(value = "author", required = false) String author){

        List<Book> bookList = bookService.getBooks(genre,available,author); //find the elements of the list by yourself

        return new ResponseEntity<>(bookList, HttpStatus.OK);

    }
}

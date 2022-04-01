package com.Service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Comparator;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class HighScore implements Comparator<HighScore> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int score;
    @DateTimeFormat(pattern = "YYYY-MM-DD")
    private LocalDate date;
    @ManyToOne
    private User user;

    @Override
    public int compare(HighScore o1,HighScore o2) {

        return ((Integer)o1.getScore()).compareTo((Integer)o2.getScore());
    }
}

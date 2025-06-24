/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
/**
 *
 * @author Fikal Alif
 */
public class GenericFilter<T> {
    public List<T> filter(List<T> data, Predicate<T> condition){
        return data.stream().filter(condition).collect(Collectors.toList());
    }
}

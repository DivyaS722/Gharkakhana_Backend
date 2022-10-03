package com.gharkakhana.service;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gharkakhana.entity.Cart;
import com.gharkakhana.entity.Food;
import com.gharkakhana.entity.NewCart;
import com.gharkakhana.entity.NewFood;
import com.gharkakhana.repository.ICartRepository;
import com.gharkakhana.repository.IFoodRepository;


@Service
public class ICartService {
	private String storagePath;

	@Autowired
	private ICartRepository ICartRepository;
	@Autowired
	private IFoodRepository IFoodRepository;
	
    public void saveToCart(NewCart[] newCarts){
        ICartRepository.deleteAll();
        ICartRepository.flush();
        Cart cart= new Cart(1,0,0,0,0,0);
        cart.setQuantity1(newCarts[0].getQuantity());
        cart.setQuantity2(newCarts[1].getQuantity());
        cart.setQuantity3(newCarts[2].getQuantity());
        if(newCarts.length>3)
            cart.setQuantity4(newCarts[3].getQuantity());
        if(newCarts.length>4)
        cart.setQuantity5(newCarts[4].getQuantity());
        if(newCarts.length>5)
        cart.setQuantity6(newCarts[5].getQuantity());
        ICartRepository.save(cart);
    }

    public void updateDB(){
        List<Cart> carts =ICartRepository.findAll();
        Cart cart = carts.get(1);

        List<Food> foods = IFoodRepository.findAll();
        foods.get(0).setQuantity(foods.get(0).getQuantity()-cart.getQuantity1());
        foods.get(1).setQuantity(foods.get(1).getQuantity()-cart.getQuantity2());
        foods.get(2).setQuantity(foods.get(2).getQuantity()-cart.getQuantity3());
        if(foods.size()>3)
        foods.get(3).setQuantity(foods.get(3).getQuantity()-cart.getQuantity4());
        if(foods.size()>4)
            foods.get(4).setQuantity(foods.get(4).getQuantity()-cart.getQuantity5());
        if(foods.size()>5)
            foods.get(5).setQuantity(foods.get(5).getQuantity()-cart.getQuantity6());
        IFoodRepository.saveAll(foods);
    }

    public List<Cart> getAllCart()
    {
        return ICartRepository.findAll();
    }

    public void addItems(NewCart[] cart){
        List<Food> foods = IFoodRepository.findAll();
        for(int i=0;i<foods.size();i++){
            foods.get(i).setQuantity(foods.get(i).getQuantity()+cart[i].getQuantity());
        }
        IFoodRepository.saveAll(foods);
    }

    public boolean addNewItem(MultipartFile file, String newFoodData) throws IOException {
        NewFood newFood = new ObjectMapper().readValue(newFoodData,NewFood.class);
        if(!file.isEmpty()) {
        if(saveFileToAssets(file))
        
            IFoodRepository.save(new Food(newFood.getId(),newFood.getName(),newFood.getPrice(),newFood.getQuantityAvailable(),"/assets/"+file.getOriginalFilename(),"",""));
        }
        return true;
    }

    public boolean addNewItemWithUrl(String newFoodData) throws IOException {
        NewFood newFood = new ObjectMapper().readValue(newFoodData,NewFood.class);
        IFoodRepository.save(new Food(newFood.getId(),newFood.getName(),newFood.getPrice(),newFood.getQuantityAvailable(),newFood.getFileDataF(),"",""));
        return true;
    }

    private boolean saveFileToAssets(MultipartFile file) throws IOException {
        Path filepath = Paths.get(storagePath, file.getOriginalFilename());
        file.transferTo(filepath);
        return true;
    }

    public int claculateTotal(NewCart[] newCart){
        int total=0;
        List<Food> foods = IFoodRepository.findAll();

        for(int i=0;i<foods.size();i++)
        {
            total+=foods.get(i).getPrice()*newCart[i].getQuantity();
        }
        return total;
    }

    public boolean itemIdAvailable(String itemId) {
        return IFoodRepository.findById(itemId).isPresent();
    }
}



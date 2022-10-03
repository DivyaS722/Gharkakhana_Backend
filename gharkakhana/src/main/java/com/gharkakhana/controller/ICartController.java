package com.gharkakhana.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.gharkakhana.entity.NewCart;
import com.gharkakhana.service.ICartService;

@RestController
@CrossOrigin(origins = "http://localhost:4200/")
public class ICartController {
	@Autowired
	private ICartService ICartService;

	@PostMapping("/cart")
	public int getTotal(@RequestBody NewCart[] cart, Model model) {
		ICartService.saveToCart(cart);
		return ICartService.claculateTotal(cart);
	}

	@RequestMapping("/changeDB")
	public boolean changeDB() {
		ICartService.updateDB();
		return true;
	}

	@PostMapping("/addToCart")
	public NewCart[] increaseQuantity(@RequestBody NewCart[] cart, Model model) {
		ICartService.addItems(cart);
		return cart;
	}

	@PostMapping("/addNewItem")
	public boolean addNewItem(@RequestParam("file") MultipartFile file, @RequestParam("newFoodItem") String newFoodData)
			throws IOException {
		return ICartService.addNewItem(file, newFoodData);
	}

	@PostMapping("/addNewItemUrl")
	public boolean addNewItemByUrl(@RequestParam("newFoodItem") String newFoodData) throws IOException {
		return ICartService.addNewItemWithUrl(newFoodData);
	}

	@PostMapping("/checkItemId")
	public boolean checkItemId(@RequestBody String itemId, Model model) {
		return !ICartService.itemIdAvailable(itemId);
	}

}

package com.pulserun.wishlist;

import com.pulserun.user.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wishlists")
public class WishlistController {

    private final WishlistService wishlistService;

    @PostMapping("/{marketId}")
    public ResponseEntity<String> addWish(
            @LoginUser Long userId,
            @PathVariable Long marketId) {

        wishlistService.addWish(userId, marketId);
        return ResponseEntity.ok("Wishlist added");
    }

    @DeleteMapping("/{marketId}")
    public ResponseEntity<String> removeWish(
            @RequestHeader("userId") Long userId,
            @PathVariable Long marketId) {

        wishlistService.removeWish(userId, marketId);
        return ResponseEntity.ok("Wishlist removed");
    }

    @GetMapping
    public ResponseEntity<List<Wishlist>> getMyWishlist(
            @RequestHeader("userId") Long userId) {

        List<Wishlist> myWishlist = wishlistService.getMyWishlist(userId);
        return ResponseEntity.ok(myWishlist);
    }
}
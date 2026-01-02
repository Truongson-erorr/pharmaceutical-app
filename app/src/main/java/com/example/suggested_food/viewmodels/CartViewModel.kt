package com.example.suggested_food.viewmodels

import androidx.lifecycle.ViewModel
import com.example.suggested_food.models.CartItemModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CartViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _cartItems = MutableStateFlow<List<CartItemModel>>(emptyList())
    val cartItems: StateFlow<List<CartItemModel>> = _cartItems

    private fun userId(): String? = auth.currentUser?.uid

    fun loadCartFromFirestore() {
        val uid = userId() ?: return

        firestore.collection("shoppingcart")
            .document(uid)
            .collection("items")
            .get()
            .addOnSuccessListener { result ->
                val list = result.documents.mapNotNull {
                    it.toObject(CartItemModel::class.java)
                }
                _cartItems.value = list
            }
    }

    fun addToCart(item: CartItemModel) {
        val current = _cartItems.value.toMutableList()
        val index = current.indexOfFirst { it.productId == item.productId }

        val updatedItem = if (index >= 0) {
            val oldItem = current[index]
            val newItem = oldItem.copy(
                quantity = oldItem.quantity + 1
            )
            current[index] = newItem
            newItem
        } else {
            current.add(item)
            item
        }

        _cartItems.value = current
        saveToFirestore(updatedItem)
    }

    private fun saveToFirestore(item: CartItemModel) {
        val uid = userId() ?: return

        firestore.collection("shoppingcart")
            .document(uid)
            .collection("items")
            .document(item.productId)
            .set(item)
    }

    fun updateQuantity(productId: String, quantity: Int) {
        val uid = userId() ?: return
        if (quantity <= 0) {
            removeItem(productId)
            return
        }

        val updated = _cartItems.value.map {
            if (it.productId == productId) {
                it.copy(quantity = quantity)
            } else it
        }

        _cartItems.value = updated

        val item = updated.find { it.productId == productId } ?: return

        firestore.collection("shoppingcart")
            .document(uid)
            .collection("items")
            .document(productId)
            .set(item)
    }

    fun removeItem(productId: String) {
        val uid = userId() ?: return

        _cartItems.value =
            _cartItems.value.filterNot { it.productId == productId }

        firestore.collection("shoppingcart")
            .document(uid)
            .collection("items")
            .document(productId)
            .delete()
    }

    fun clearCart() {
        val uid = userId() ?: return

        _cartItems.value = emptyList()

        firestore.collection("shoppingcart")
            .document(uid)
            .collection("items")
            .get()
            .addOnSuccessListener { result ->
                result.documents.forEach { it.reference.delete() }
            }
    }

    fun totalPrice(): Double {
        return _cartItems.value.sumOf { it.price * it.quantity }
    }
}

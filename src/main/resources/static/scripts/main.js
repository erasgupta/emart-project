const products = [
      { id: 1, name: "Crystal Tea Light", price: 75, image: "crystalTeaLight.jpg" },
      { id: 2, name: "Glass Candle", price: 120, image: "crystalTeaLight.jpg"  },
      { id: 3, name: "Mini Bubble Candle", price: 50, image: "crystalTeaLight.jpg"  },
      { id: 4, name: "Rose Candle", price: 60, image: "crystalTeaLight.jpg"  }
    ];

    let cart = [];

    const productList = document.getElementById("product-list");
    const cartList = document.getElementById("cart-list");

    function renderProducts() {
      productList.innerHTML = "";
      products.forEach(product => {
        const div = document.createElement("div");
        div.className = "product";
        div.innerHTML = `
		  <img src="/images/crystalTeaLight.jpg" alt="candle">
          <br/>
		  <strong>${product.name}</strong> ₹${product.price}
          <br/>
          <button onclick="addToCart(${product.id})">Add</button>
        `;
        productList.appendChild(div);
      });
    }

    function renderCart() {
      cartList.innerHTML = "";
      if (cart.length === 0) {
        cartList.innerHTML = "<p>Cart is empty.</p>";
        return;
      }
      cart.forEach((item, index) => {
        const div = document.createElement("div");
        div.className = "cart-item";
        div.innerHTML = `
          <span>${item.name} - $${item.price}</span>
          <button class="remove" onclick="removeFromCart(${index})">Remove</button>
        `;
        cartList.appendChild(div);
      });
    }

    function addToCart(id) {
      const product = products.find(p => p.id === id);
      cart.push(product);
      renderCart();
    }

    function removeFromCart(index) {
      cart.splice(index, 1);
      renderCart();
    }

    // Initial Render
    renderProducts();
    renderCart();
 // Initialize the page
        document.addEventListener('DOMContentLoaded', function() {
            displayProducts(products);
            updatePriceDisplay();
        });

        // Scroll to search section
        function scrollToSearch() {
            document.getElementById('search-section').scrollIntoView({ behavior: 'smooth' });
        }

        // Display products
        function displayProducts(productList) {
            const container = document.getElementById('productsContainer');
            const noResults = document.getElementById('noResults');
            
            if (productList.length === 0) {
                container.innerHTML = '';
                noResults.classList.remove('d-none');
                return;
            }
            
            noResults.classList.add('d-none');
            
            container.innerHTML = productList.map(product => `
                <div class="col-lg-3 col-md-4 col-sm-6">
                    <div class="product-card">
                        <div class="product-image">
                            <i class="${product.icon}"></i>
                        </div>
                        <div class="product-info">
                            <div class="product-title">${product.name}</div>
                            <div class="product-price">$${product.price}</div>
                            <div class="product-category">${getCategoryName(product.category)}</div>
                            <p class="text-muted small">${product.description}</p>
                        </div>
                    </div>
                </div>
            `).join('');
        }

        // Get category display name
        function getCategoryName(category) {
            const categories = {
                'sofa': 'Sofas & Couches',
                'chair': 'Chairs',
                'table': 'Tables',
                'bed': 'Beds',
                'storage': 'Storage',
                'decor': 'Home Decor'
            };
            return categories[category] || category;
        }

        // Basic search function
        function performSearch() {
            const searchTerm = document.getElementById('basicSearch').value.toLowerCase();
            filteredProducts = products.filter(product => 
                product.name.toLowerCase().includes(searchTerm) ||
                product.description.toLowerCase().includes(searchTerm) ||
                getCategoryName(product.category).toLowerCase().includes(searchTerm)
            );
            displayProducts(filteredProducts);
        }

        // Advanced search function
        function performAdvancedSearch() {
            const searchTerm = document.getElementById('advancedSearch').value.toLowerCase();
            const category = document.getElementById('categoryFilter').value;
            const minPrice = parseFloat(document.getElementById('minPrice').value) || 0;
            const maxPrice = parseFloat(document.getElementById('maxPrice').value) || Infinity;
            
            filteredProducts = products.filter(product => {
                const matchesSearch = !searchTerm || 
                    product.name.toLowerCase().includes(searchTerm) ||
                    product.description.toLowerCase().includes(searchTerm);
                
                const matchesCategory = !category || product.category === category;
                const matchesPrice = product.price >= minPrice && product.price <= maxPrice;
                
                return matchesSearch && matchesCategory && matchesPrice;
            });
            
            displayProducts(filteredProducts);
        }

        // Clear advanced search
        function clearAdvancedSearch() {
            document.getElementById('advancedSearch').value = '';
            document.getElementById('categoryFilter').value = '';
            document.getElementById('minPrice').value = '';
            document.getElementById('maxPrice').value = '';
            document.getElementById('priceRange').value = 2500;
            updatePriceDisplay();
            filteredProducts = [...products];
            displayProducts(filteredProducts);
        }

        // Update price display
        function updatePriceDisplay() {
            const priceRange = document.getElementById('priceRange');
            const priceDisplay = document.getElementById('priceDisplay');
            const maxPrice = parseInt(priceRange.value);
            priceDisplay.textContent = `$0 - $${maxPrice.toLocaleString()}`;
            
            // Update max price input
            document.getElementById('maxPrice').value = maxPrice;
        }

        // Sort products
        function sortProducts() {
            const sortBy = event.target.value;
            
            switch(sortBy) {
                case 'name':
                    filteredProducts.sort((a, b) => a.name.localeCompare(b.name));
                    break;
                case 'price-low':
                    filteredProducts.sort((a, b) => a.price - b.price);
                    break;
                case 'price-high':
                    filteredProducts.sort((a, b) => b.price - a.price);
                    break;
                case 'category':
                    filteredProducts.sort((a, b) => a.category.localeCompare(b.category));
                    break;
            }
            
            displayProducts(filteredProducts);
        }

        // Enter key search functionality
        document.getElementById('basicSearch').addEventListener('keypress', function(e) {
            if (e.key === 'Enter') {
                performSearch();
            }
        });

        document.getElementById('advancedSearch').addEventListener('keypress', function(e) {
            if (e.key === 'Enter') {
                performAdvancedSearch();
            }
        });

        // Real-time price range update
        document.getElementById('priceRange').addEventListener('input', function() {
            updatePriceDisplay();
            if (document.getElementById('advancedSearch').value || 
                document.getElementById('categoryFilter').value || 
                document.getElementById('minPrice').value) {
                performAdvancedSearch();
            }
        });
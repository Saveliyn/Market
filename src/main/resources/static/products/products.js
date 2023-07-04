angular.module('app').controller('productsController', function ($scope, $http) {
    const contextPath = 'http://localhost:8189/happy';

    $scope.showProductsPage = function (pageIndex = 1) {
        $http({
            url: contextPath + '/api/v1/products',
            method: 'GET',
            params: {
                title: $scope.filter ? $scope.filter.title : null,
                min_price: $scope.filter ? $scope.filter.min_price : null,
                max_price: $scope.filter ? $scope.filter.max_price : null,
                p: pageIndex
            }
        }).then(function (response) {
            $scope.ProductsPage = response.data;
        });
    };

    $scope.generatePagesIndexes = function (startPage, endPage) {
        let arr = [];
        for (let i = startPage; i < endPage + 1; i++) {
            arr.push(i);
        }
        return arr;
    }

    $scope.addToCart = function (productId) {
        $http.get(contextPath + '/api/v1/cart/add/' + productId)
            .then(function (response) {
            });
    }

    $scope.showProductInfo = function (productId) {
            $http.get(contextPath + '/api/v1/products/' + productId).then(function (response) {
            if(response.data.title == "Молоко") {
                            alert("Хорошее молоко");
                            } else if(response.data.title == "Корм для Собак") {
                            alert("Прекрасно подходит для рыжих собак");
                            }
                            else{
                            alert(response.data.title + " по цене: " + response.data.price);
                            }

            });
        }

    $scope.createOrder = function () {
        $http.get(contextPath + '/api/v1/orders/create')
            .then(function (response) {
            });
    }

    $scope.showProductsPage();
});
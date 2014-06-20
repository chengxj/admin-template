  var services = angular.module('routeResolverServices', []);

    //Must be a provider since it will be injected into module.config()    
    services.provider('routeResolver', function () {

        this.$get = function () {
            return this;
        };

        this.routeConfig = function () {
            var viewsDirectory = '/tpl/views/',
                controllersDirectory = '/js/controllers/',

            setBaseDirectories = function (viewsDir, controllersDir) {
                viewsDirectory = viewsDir;
                controllersDirectory = controllersDir;
            },

            getViewsDirectory = function () {
                return viewsDirectory;
            },

            getControllersDirectory = function () {
                return controllersDirectory;
            };

            return {
                setBaseDirectories: setBaseDirectories,
                getControllersDirectory: getControllersDirectory,
                getViewsDirectory: getViewsDirectory
            };
        }();

        this.route = function (routeConfig) {

            var resolve = function (baseName, path) {
                if (!path) path = '';

                var routeDef = {};
                routeDef.templateUrl = routeConfig.getViewsDirectory() + path +"/views/" + baseName + ".html";
                var baseNames = baseName.split("_");
                for(var i = 0; i < baseNames.length; i ++) {
                	baseNames[i] = baseNames[i].replace(/\b\w+\b/g, function(word){
                        return word.substring(0,1).toUpperCase()+word.substring(1);}
                    );
                }
                var paths = path.split("/");
                var _path =paths[paths.length-1].replace(/\b\w+\b/g, function(word){
                  return word.substring(0,1).toUpperCase()+word.substring(1);}
                 );
                var _baseName = baseNames.join("");
                routeDef.controller = _path + _baseName + 'Ctrl';
                routeDef.resolve = {
                    load: ['$q', '$rootScope', function ($q, $rootScope) {
                        var dependencies = [routeConfig.getControllersDirectory() + path +"/js/"  + _path +  _baseName + 'Ctrl.js'];
                        return resolveDependencies($q, $rootScope, dependencies);
                    }]
                };

                return routeDef;
            },

            resolveDependencies = function ($q, $rootScope, dependencies) {
                var defer = $q.defer();
                require(dependencies, function () {
                    defer.resolve();
                    $rootScope.$apply()
                });

                return defer.promise;
            };

            return {
                resolve: resolve
            }
        }(this.routeConfig);
    });

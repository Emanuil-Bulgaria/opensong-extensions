{
  description = "A very basic flake";

  inputs = {
    nixpkgs.url = "https://channels.nixos.org/nixpkgs-unstable/nixexprs.tar.zst";
  };

  outputs = { nixpkgs, ... }@inputs: let 
      systems = [ "x86_64-linux" ];
      forEachSystem = f: nixpkgs.lib.genAttrs systems (system: f {
        pkgs = import nixpkgs { 
          inherit system;
          config = {
            allowUnfree = true;
          };
        };
      });
    in {
    packages = builtins.mapAttrs (system: pkgs: {
      hello = pkgs.hello;

      default = inputs.self.packages.${system}.hello;
    }) inputs.nixpkgs.legacyPackages;

    devShells = forEachSystem ({ pkgs }: let 
        ffmpeg = (pkgs.ffmpeg.overrideAttrs (old: {

          patches = (old.patches or []) ++ [
                    (pkgs.fetchurl {
                      name = "chore-Add-nonfree-libndi_newtek-device.patch";
                      url = "https://aur.archlinux.org/cgit/aur.git/plain/chore-Add-nonfree-libndi_newtek-device.patch?h=ffmpeg-ndi";
                      hash = "sha256-SH9to+C6C3Wkoes0vxpPmunnfIfAH1dzbdOTwMQ3hcs=="; 
                    })
                  ];

            buildInputs = old.buildInputs ++ [ pkgs.ndi-6 ];
            configureFlags = old.configureFlags ++ [ 
               "--enable-nonfree" "--enable-libndi_newtek" ];
            extraConfigure = (old.extraConfigure or "") + ''
              --extra-cflags="-I${pkgs.ndi-6}/include" \
              --extra-ldflags="-L${pkgs.ndi-6}/lib"
            '';

            postFixup = ''
              patchelf --add-rpath "${pkgs.ndi-6}/lib" $bin/bin/ffmpeg
              patchelf --add-rpath "${pkgs.ndi-6}/lib" $bin/bin/ffprobe
              patchelf --add-rpath "${pkgs.ndi-6}/lib" $bin/bin/ffplay
              ${old.postFixup or ""}
            '';
          }));
        fhsenv = pkgs.buildFHSEnv {
          name = "ndi-kotlin-shell";
          targetPkgs = pkgs: [
              pkgs.ndi-6
              ffmpeg
              pkgs.jdk25
              pkgs.avahi
              pkgs.libGL
              pkgs.libglvnd
          ];
          profile = ''
            export IN_FHS_CONTAINER=1
          '';
          runScript = "nu";
        };
      in {
      default = pkgs.mkShell {
        packages = with pkgs; [ 
          ndi-6
          jdk25
          sops
          ffmpeg
        ];

        NDI_LIBRARY_PATH = "${pkgs.ndi-6}/lib/libndi.so";

        # sops -e --input-type dotenv --output-type dotenv secrets.env > secrets.enc.env
        shellHook = ''
          sops -d --output-type dotenv secrets.enc.env > .env
          exec ${fhsenv}/bin/ndi-kotlin-shell
        '';
      };
    });
  };
}

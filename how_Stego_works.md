# HOW STEGO WORKS

Stego uses *least significant bit* (LSB) encoding to store text input by the user inside an image without changing the appearance of the image.

This image is stored as a *bitmap* - an array of binary data. Each pixel in an image has 3 colour channels: red, green, and blue - each storing 8 bits, representing a colour value from 0 to 256. In each channel, only the last binary digit is changed. This means the colour value only changes by a value of 1. The visual difference is impossible to notice.

<br>

## Image Encoder

Before encoding the text, the header needs to be created. The header starts with a fixed key, and then stores the length of the message, which is used when Stego decodes an image into the original text.

Stego checks weather the image is large enough to store the text. In other words, weather the length of the header and text (in bits) is less than the resolution of the image multiplied by 3 for each channel.

Stego proceeds to encode the image if it's large enough. The header is encoded first, and the text follows. They are converted into binary and, bit by bit, encoded into the image by changing the LSB of each channel in as many pixels as needed.

<br>

## Image Decoder

Stego decodes the header and text by extracting the LSB of each channel in every pixel.

When decoding an image Stego first reads the fixed key. This is used to identify weather the image has been encoded using Stego. If the image was not encoded using the specific format used by Stego, trying to decode it would produce meaningless text.

If the key exists, Stego proceeds to read the length of the text. This tells Stego how many pixels and channels the message is stored in. Only the encoded text is decoded from the image. Trying to decode data past the length of the text would produce meaningless text.

Once the length of the text is know, Stego decodes the text and converts it back into a readable format.

<br>

## Limitations

Stego stores images as a bitmap, meaning the pixels are stored as binary data. This causes some limitations:
- Saving as JPG image may corrupt the encoded data. JPG images use lossy compression.
- Resizing the image may corrupt the encoded data.
- Editing the image in any way may corrupt the encoded data.

Lossless formats such as PNG work best.





